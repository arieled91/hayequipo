import {environment} from "../../environments/environment";
import {HttpParams} from "@angular/common/http/src/params";

export default class Api{
  public static BASE_URL = environment.apiUrl;

  public static request(endpoint: String, args: String): string{
    const argPrefix = (args !== null && args !== "" ? "?" : "");
    return args != null ? endpoint+argPrefix+args : endpoint+'';
  }

  public static addPageParams(params: HttpParams, page: number | null, sort?: string | null, order?: string | "asc", size?: number | null): HttpParams{
    if(page!==null) params = params.append('page', Number(page).toString());
    if(size!==null) params = params.append('size', Number(size).toString());
    if(sort!==null) params = params.append('sort', Number(sort).toString()+','+Number(order).toString());

    return params;

  }

  public static pageParams(page: number | null, sort?: string | null, order?: string | "asc", size?: number | null){
    let params  = "";

    if(page!==null) params += `page=${page}`;
    if(size!==null) params += `size=${size}`;
    if(sort!==null) params += `sort=${sort},${order}`;

    return params;
  }
}
