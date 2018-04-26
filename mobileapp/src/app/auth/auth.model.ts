export interface TokenResponse{
  token: string;
}

export class User{
  firstName : string = "";
  lastName  : string = "";
  email     : string = "";
  privileges: string[] = [];
}

export class UserRegistration{
  firstName : string = null;
  lastName  : string = null;
  email     : string = null;
  password  : string = null;
}
