import { Component, OnInit } from '@angular/core';
import {AuthService} from "../service/auth.service";
import {PagedList} from "../../common/common.model";
import {User} from "../auth.model";
import {MatTableDataSource} from "@angular/material";

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent implements OnInit {

  users : PagedList<User> ;
  dataSource : MatTableDataSource<User>;
  displayedColumns = ['email', 'firstName', 'lastName'];

  constructor(private authService:AuthService) {
    this.authService.listUsers().subscribe(users => {
      this.users = users;
      this.dataSource = new MatTableDataSource(users.data);
    });
  }

  ngOnInit() {
  }

  applyFilter(filterValue: string) {
    filterValue = filterValue.trim(); // Remove whitespace
    filterValue = filterValue.toLowerCase(); // MatTableDataSource defaults to lowercase matches
    this.dataSource.filter = filterValue;
  }

}
