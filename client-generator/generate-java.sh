#This is for server generation
#java -jar openapi-generator-cli.jar generate \
#    -i http://localhost:18081/v3/api-docs \
#    -g spring \
#    -o ./java-client/

swagger-codegen generate -i http://localhost:18081/v3/api-docs -l java -o ../client-java/

