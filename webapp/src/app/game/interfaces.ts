import DateTimeFormat = Intl.DateTimeFormat;

export interface Game {
  description : String;
  date        : DateTimeFormat;
  location    : Location;
}
export interface Location {
  latitude    : Number;
  longitude   : Number;
  description : String;
}
