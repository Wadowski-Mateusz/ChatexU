db.createUser(
  {
      user: "chatserverapp",
      pwd: "XVDGNdrBIm8XWRs",
      roles: [
          {
              role: "readWrite",
              db: "chatexu"
          }
      ]
  }
);