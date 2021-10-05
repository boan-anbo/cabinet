#This is for server generation
openapi-generator-cli generate -i http://localhost:18081/v3/api-docs  -g rust  -o ./clients/rust

#swagger-codegen generate -i http://localhost:18081/v3/api-docs -l java -o ../client-fx/cabinet-rest-client/

