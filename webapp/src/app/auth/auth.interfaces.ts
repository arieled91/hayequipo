export interface TokenResponse{
  token: String;
}

export class User{
  firstName : String = "";
  lastName  : String = "";
  email     : String = "";
  privileges: String[] = [];
}
