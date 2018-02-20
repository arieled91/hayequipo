export class Api{
  private static BACKEND_URL = "http://localhost:8080/";

  public static url(){
    return this.BACKEND_URL+"api/";
  }
  public static request(endpoint: String, args: String){
    const base = Api.url()+endpoint;
    const argPrefix = (args !== null && args !== "" ? "?" : "");
    return args != null ? base+argPrefix+args : base;
  }
}
