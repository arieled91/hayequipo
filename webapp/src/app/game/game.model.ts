import {Moment} from "moment";

export class Game {
  id          : Number = null;
  description : String = null;
  dateTime    : Moment = null;
  location    : Location = new Location();
  capacity    : Number = null;
  currentUserJoined : boolean = false;
}

export class Location {
  latitude    : Number = null;
  longitude   : Number = null;
  description : String = null;
  address     : String = null;
}
