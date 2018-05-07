import {environment} from "../../environments/environment";

export default class Api{
  public static BASE_URL = environment.apiUrl;

  public static request(endpoint: String, args: String): string{
    const argPrefix = (args !== null && args !== "" ? "?" : "");
    return args != null ? endpoint+argPrefix+args : endpoint+'';
  }

  public static pageParams(page: number | null, sort?: string | null, order?: string | "asc", size?: number | null){
    let params  = "";

    if(page!==null) params += `page=${page}`;
    if(size!==null) params += `size=${size}`;
    if(sort!==null) params += `sort=${sort},${order}`;

    return params;
  }
}
