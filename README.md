**This is a basic dropbox implementation which performs the following operations**

  1. Allows users to login using username and password
  2. User can upload / download / delete files to dropbox

The application is implementeed using Java Springboot and uses S3 as persistent storage solution.
I am using Postgres SQL for storing file metadata and user details.

To connect to your own S3 instance, update the client key and secret in the application.yml
To connect to your postgres instance update application.properties.
