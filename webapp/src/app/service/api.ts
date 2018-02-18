export class Api{
  private static BACKEND_URL = "http://localhost:8080/";

  public static url(){
    return this.BACKEND_URL+"api/";
  }
  public static request(endpoint: String, args: String){
    let base = Api.url()+endpoint;
    return args != null ? base+"?"+args : base;
  }
}
