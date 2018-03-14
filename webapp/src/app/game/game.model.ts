import {Moment} from "moment";

export class Game {
  id          : Number = null;
  description : String = null;
  dateTime    : Moment = null;
  location    : Location = new Location();
  capacity    : Number = null;
  currentUserJoined : boolean = false;
  status      : String = null;
  players     : Player[] = [];
}

export class Location {
  latitude    : Number = null;
  longitude   : Number = null;
  description : String = null;
  address     : String = null;
}

export class Player{
  firstName : String = "";
  lastName  : String = "";
  email     : String = "";
}

export const fieldTypes = [
  {capacity: 10, label : "Fútbol 5"},
  {capacity: 12, label : "Fútbol 6"},
  {capacity: 14, label : "Fútbol 7"},
  {capacity: 22, label : "Profesional"}
];

export enum GameStatus{
  OPEN = "OPEN",
  CLOSED = "CLOSED"
}
