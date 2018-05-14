import {Links} from "../common/common.model";

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

export class Role{
  name : string;
  _links: Links;
}

export enum Privileges {
  FULL_ACCESS = "FULL_ACCESS"
}

