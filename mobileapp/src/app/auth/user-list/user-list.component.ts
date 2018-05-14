import {AuthService} from "../service/auth.service";
import {PagedList} from "../../common/common.model";
import {Role, User} from "../auth.model";
import {AfterContentInit, Component, ViewChild} from '@angular/core';
import {MatPaginator, MatSort, MatTableDataSource} from '@angular/material';
import {merge} from 'rxjs';
import {map, startWith, switchMap} from 'rxjs/operators';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent implements AfterContentInit {

  title = "Usuarios";
  users : PagedList<User> ;
  displayedColumns = ['email', 'name'];
  dataSource : MatTableDataSource<User> = new MatTableDataSource();
  resultsLength = 0;
  isLoadingResults = true;
  pageSize = 30;
  searchQuery : string = '';
  roles : Role[] = [];
  userPrivileges;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private authService:AuthService) {}

  ngAfterContentInit() {
    // If the user changes the sort order, reset back to the first page.
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    this.find();

    this.authService.getUserPrivileges().subscribe(data => this.userPrivileges=data);
    this.authService.listRoles().subscribe(data => this.roles=data);
  }

  applyFilter() {
    if(this.searchQuery.length<3 && this.searchQuery.length!=0) return;
    this.find();
  }

  find(){
    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          return this.authService.listUsers(this.searchQuery, this.sort.active, this.sort.direction, this.paginator.pageIndex, this.pageSize);
        }),
        map(data => {
          // Flip flag to show that loading has finished.
          this.isLoadingResults = false;
          this.resultsLength = data.page.totalElements;

          return data.data;
        })
      ).subscribe(data => {
        this.dataSource.data = data;
        this.isLoadingResults = false;
      });
  }
}
