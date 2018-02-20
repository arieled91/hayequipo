export interface Game {
  description : String;
  dateTime    : Date;
  location    : Location;
  currentUserJoined : boolean;
}

export interface Location {
  latitude    : Number;
  longitude   : Number;
  description : String;
}
