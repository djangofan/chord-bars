# chord-bars azure function


## Execution

    mvn clean package azure-functions:run

I generally use VSCode Azure integration to run this service locally while debugging.

Also, I use VSCode plugin to upload project to Azure.

## Usage

Example: 

    http://localhost:7071/api/bar?scale=5&bg=fff&fg=000&text=[Dm7b5,Edim7,A6,D7]&font=Pacaembu&style=0


    https://chord-bars.azurewebsites.net/api/bar?scale=5&bg=fff&fg=000&text=[Dm7b5,Edim7,A6,D7]&font=Pacaembu&style=0


    