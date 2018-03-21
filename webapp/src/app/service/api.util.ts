export default class Api{
  public static BASE_URL = "http://localhost:8888";
  // public static BASE_URL = "http://10.28.133.81:8888";

  public static URL = Api.BASE_URL+"/api";

  public static request(endpoint: String, args: String){
    const base = Api.URL+endpoint;
    const argPrefix = (args !== null && args !== "" ? "?" : "");
    return args != null ? base+argPrefix+args : base;
  }
}
