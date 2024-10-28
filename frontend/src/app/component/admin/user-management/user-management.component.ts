import { Component } from '@angular/core';
import { UserDTO } from '../../../dto/auth/UserDTO';
import { UserService } from '../../../service/user.service';

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrl: './user-management.component.css'
})
export class UserManagementComponent {

  users: UserDTO[] = [];

  currentPage = 0;
  pageSize = 4;
  totalPages = 0;

  constructor(
    private userSerivce: UserService,
    ) { }

  ngOnInit(): void {
    this.loadRestaurants();
  }
  loadRestaurants(): void {
    this.userSerivce.getAllUsers(this.currentPage, this.pageSize)
    .subscribe(response => {
      this.users = response.content;
      this.totalPages = response.totalPages;
    });
  }
}
