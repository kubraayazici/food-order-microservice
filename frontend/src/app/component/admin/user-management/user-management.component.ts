import { Component } from '@angular/core';
import { UserDTO } from '../../../dto/auth/UserDTO';
import { UserService } from '../../../service/user.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrl: './user-management.component.css'
})
export class UserManagementComponent {

  users: UserDTO[] = [];
  displayedColumns: string[] = ['username', 'email', 'roles', 'status', 'actions'];
  isLoading = false;
  includeInactive = false;
  showUserForm = false;
  selectedUser?: UserDTO;
  errorMessage : string ='';

  currentPage = 0;
  pageSize = 4;
  totalPages = 0;

  constructor(
    private userService: UserService,
  
    ) { }

  ngOnInit(): void {
    this.loadUsers();

  }
  
  loadUsers(): void {
    this.isLoading = true;
    this.userService.getUsers(this.includeInactive).subscribe({
      next: (users) => {
        this.users = users;
        this.isLoading = false;
        console.log(users);
        
      },
      error: (error) => {
        this.showError('Error loading users');
        this.isLoading = false;
      }
    });
  }

  openUserForm(user?: UserDTO): void {
    this.selectedUser = user;
    this.showUserForm = true;
  }

  closeUserForm(): void {
    this.selectedUser = undefined;
    this.showUserForm = false;
  }

  showError(message: string): void {
    this.errorMessage = message;
    setTimeout(() => {
      this.errorMessage = '';
    }, 3000);
  }

  toggleUserStatus(user: UserDTO): void {
    this.isLoading = true;
    const operation = (user.active ?
      this.userService.deactivateUser(user.userId) :
      this.userService.reactivateUser(user.userId)) as Observable<UserDTO>;

    operation.subscribe({
      next: () => {
        this.showError(`User ${user.active ? 'deactivated' : 'reactivated'} successfully`);
        this.loadUsers();
      },
      error: (error) => {
        this.showError(`Error ${user.active ? 'deactivating' : 'reactivating'} user`);
        this.isLoading = false;
      }
    });
  }
}
