export class Game {
  description : String;
  dateTime    : Date;
  location    : Location;
  currentUserJoined : boolean;
}

export class Location {
  latitude    : Number;
  longitude   : Number;
  description : String;
}
