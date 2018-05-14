export class Page {
  size: number = 0;
  totalElements: number = 0;
  totalPages: number = 0;
  number: number = 0;
}

export class PagedList<T>{
  data: Array<T> = [];
  page: Page = new Page();
}

export class Links{
  self : Link = null;
}

export class Link{
  href : string = "";
}
