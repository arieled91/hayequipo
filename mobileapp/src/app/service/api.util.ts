import {environment} from "../../environments/environment";

export default class Api{
  public static BASE_URL = environment.apiUrl;

  public static request(endpoint: String, args: String): string{
    const argPrefix = (args !== null && args !== "" ? "?" : "");
    return args != null ? endpoint+argPrefix+args : endpoint+'';
  }
}
