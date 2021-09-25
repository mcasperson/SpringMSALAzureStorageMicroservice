Run with the following PowerShell:

```
$env:CLIENT_SECRET="Application client secret"
$env:CLIENT_ID="Application client ID"
$env:TENANT_ID="Azure AD tenant ID"
$env:API_URL_ID="Application API URI"
$env:DB_NAME="The name of the database"
$env:DB_USERNAME="The database username"
$env:DB_PASSWORD="The database password"

.\mvnw spring-boot:run
```


Or the following Bash:

```
export CLIENT_SECRET="Application client secret"
export CLIENT_ID="Application client ID"
export TENANT_ID="Azure AD tenant ID"
export API_URL_ID="Application API URI"
export DB_NAME="The name of the database"
export DB_USERNAME="The database username"
export DB_PASSWORD="The database password"

./mvnw spring-boot:run 
```