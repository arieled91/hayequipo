export interface TokenResponse{
  token: String;
}

export class User{
  firstName : String = "";
  lastName  : String = "";
  email     : String = "";
  privileges: String[] = [];
}

export class UserRegistration{
  firstName : String = null;
  lastName  : String = null;
  email     : String = null;
  password  : String = null;
}
