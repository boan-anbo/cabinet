#This is for server generation
java -jar openapi-generator-cli.jar generate -i http://localhost:18081/v3/api-docs  -g java  -o ./cabinet-api-java-client/

#swagger-codegen generate -i http://localhost:18081/v3/api-docs -l java -o ../client-fx/cabinet-rest-client/

