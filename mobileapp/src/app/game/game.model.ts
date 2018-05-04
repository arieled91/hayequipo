import {Moment} from "moment";

export class Game {
  id          : Number = null;
  description : string = "";
  dateTime    : Moment = null;
  location    : Location = new Location();
  capacity    : number = 0;
  currentUserJoined : boolean = false;
  status      : String = null;
  players     : Player[] = [];
}

export class Location {
  latitude    : number = 0;
  longitude   : number = 0;
  description : String = null;
  address     : string = "";
}

export class Player{
  firstName : string = "";
  lastName  : string = "";
  email     : string = "";
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
