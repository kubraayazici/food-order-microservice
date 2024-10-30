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

  // Pagination
  currentPage = 0;
  pageSize = 4;
  totalPages = 0;
  totalElements = 0;

  constructor(
    private userService: UserService,
  
    ) { }

  ngOnInit(): void {
    this.loadUsers();
  }
  
  loadUsers(): void {
    this.isLoading = true;
    this.userService.getUsers(this.includeInactive, this.currentPage, this.pageSize).subscribe({
      next: (response) => {
        this.users = response.content;
        this.totalPages = response.totalPages;
        this.totalElements = response.totalElements;
        this.isLoading = false;
        
      },
      error: (error) => {
        this.showError('Error loading users');
        this.isLoading = false;
      }
    });
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadUsers();
  }

  toggleInactiveUsers(): void {
    this.includeInactive = !this.includeInactive;
    this.currentPage = 0; // Reset to first page when toggling
    this.loadUsers();
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
