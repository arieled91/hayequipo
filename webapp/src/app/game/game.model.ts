export class Game {
  id          : Number = null;
  description : String = null;
  dateTime    : Date = null;
  location    : Location = new Location();
  currentUserJoined : boolean = false;
}

export class Location {
  latitude    : Number = null;
  longitude   : Number = null;
  description : String = null;
}
