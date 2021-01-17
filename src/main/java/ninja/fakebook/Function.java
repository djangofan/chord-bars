package ninja.fakebook;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import org.json.JSONArray;

import javax.imageio.ImageIO;

public class Function {

    @FunctionName("bar")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger started processing a bar.");

        final String scale = Optional.ofNullable(request.getQueryParameters().get("scale")).orElse("4");
        final String bg = Optional.ofNullable(request.getQueryParameters().get("bg")).orElse("cf3");
        final String fg = Optional.ofNullable(request.getQueryParameters().get("fg")).orElse("000");
        final String text = Optional.ofNullable(request.getQueryParameters().get("text")).orElse("['%']");
        final String font = Optional.ofNullable(request.getQueryParameters().get("font")).orElse("Alegreya");
        final String style = Optional.ofNullable(request.getQueryParameters().get("style")).orElse("0");

        Color bgColor = parseColor(bg);
        Color fgColor = parseColor(fg);
        int imgScale = Integer.parseInt(scale);
        int fontStyle = Integer.parseInt(style);
        Font baseFont = new Font(font, fontStyle, 8);

        BarGenerator barGenerator = new BarGenerator(imgScale, text, baseFont);

        barGenerator.setGraphicsColor(bgColor);
        barGenerator.drawBackground();

        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(text);
            if (jsonArray.length() < 0 || jsonArray.length() == 3 || jsonArray.length() > 4) {
                throw new IllegalArgumentException("Invalid chord list passed to function.");
            }
            DividedBy barStyle = DividedBy.getStyle(jsonArray);

            if (jsonArray.length() == 1) {
                barGenerator.setGraphicsColor(fgColor);
                barGenerator.drawOutline();
                barGenerator.drawDividers(barStyle);
                barGenerator.drawBar(barStyle);
            }
    
            if (jsonArray.length() == 2) {
                barGenerator.setGraphicsColor(fgColor);
                barGenerator.drawOutline();
                barGenerator.drawDividers(barStyle);
                barGenerator.drawBar(barStyle);
            }
            
            if (jsonArray.length() == 4) {
                barGenerator.setGraphicsColor(fgColor);
                barGenerator.drawOutline();
                barGenerator.drawDividers(barStyle);
                barGenerator.drawBar(barStyle);
            }
        } catch (RuntimeException e) {
            context.getLogger().info("Error parsing chords from url: " + e.getMessage());
            e.printStackTrace();
            barGenerator.drawError("Error reading chords in url.");
        }

        try {
            return request.createResponseBuilder(HttpStatus.OK)
                .header("Content-Type", "image/" + BarGenerator.IMAGE_TYPE)
                .body(toByteArray(barGenerator.getBufferedImage(), BarGenerator.IMAGE_TYPE))
                .build();
        } catch (IOException e) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("There was an error: " + e.getMessage()).build();
        }
        
    }

    protected static Color parseColor(String hexcolor) {
        if (hexcolor.length() == 3) {
            hexcolor = hexcolor.replaceAll("([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])", "$1$1$2$2$3$3");
        } else if (hexcolor.length() != 6) {
            throw new RuntimeException("Illegal color argument.  Must be 3 or 6 characters.");
        }
        return new Color(
                Integer.valueOf(hexcolor.substring(0, 2), 16),
                Integer.valueOf(hexcolor.substring(2, 4), 16),
                Integer.valueOf(hexcolor.substring(4, 6), 16));
    }

    protected byte[] toByteArray(BufferedImage bufferedImage, String type) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, type, baos);
        return baos.toByteArray();
    }

}
