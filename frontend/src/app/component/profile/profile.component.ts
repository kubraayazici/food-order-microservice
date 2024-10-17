import { Component, OnInit } from '@angular/core';
import { UserDTO } from '../../dto/auth/UserDTO';
import { UserService } from '../../service/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {
  user : UserDTO | null = null;

  constructor(
    private userService: UserService
  ){}

  ngOnInit(): void {
    this.userService.user$.subscribe(user => {
      this.user = user;
    });
  }
}
