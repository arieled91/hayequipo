export class Api{
  public static BASE_URL = "http://192.168.0.105:8080";

  public static URL = Api.BASE_URL+"/api";

  public static request(endpoint: String, args: String){
    const base = Api.URL+endpoint;
    const argPrefix = (args !== null && args !== "" ? "?" : "");
    return args != null ? base+argPrefix+args : base;
  }
}
