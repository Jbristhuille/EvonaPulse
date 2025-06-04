/*****************************************************************************
 * @Author                : Jbristhuille<jbristhuille@gmail.com>             *
 * @CreatedDate           : 2025-06-04 14:17:26                              *
 * @LastEditors           : Jbristhuille<jbristhuille@gmail.com>             *
 * @LastEditDate          : 2025-06-04 15:08:16                              *
 ****************************************************************************/

/* SUMMARY
  * Imports
  * submitForm - Send credentials to the server for login
*/

/* Imports */
import { Component, inject } from '@angular/core';
import { NonNullableFormBuilder, Validators, ReactiveFormsModule  } from '@angular/forms';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzFormModule } from 'ng-zorro-antd/form';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzCardModule } from 'ng-zorro-antd/card';
import { HttpClient } from '@angular/common/http';
import { environment as env } from '../../../environments/environment';
import { NzMessageService } from 'ng-zorro-antd/message';
import { Router } from '@angular/router';
/***/

@Component({
  selector: 'view-login',
  imports: [
    NzFormModule,
    NzInputModule,
    NzButtonModule,
    ReactiveFormsModule,
    NzCardModule,
  ],
  templateUrl: './login.view.html',
  styleUrl: './login.view.scss',
  standalone: true
})
export class LoginView {
  private fb = inject(NonNullableFormBuilder);
  private httpClient = inject(HttpClient);

  public email: string = '';
  public password: string = '';

  public loginForm = this.fb.group({
    email: [this.email, [Validators.required, Validators.email]],
    password: [this.password, [Validators.required, Validators.minLength(6)]]
  });

  constructor(private message: NzMessageService,
              private router: Router) {
  }

  /**
  * submitForm - Send credentials to the server for login
  * This method checks if the form is valid, and if so, sends a POST request
  */
  public submitForm(): void {
    if (this.loginForm.valid) {
      const { email, password } = this.loginForm.value;

      this.httpClient.post<{token: string}>(`${env.API_URL}/api/auth/login`, { email, password })
        .subscribe({
          next: (response) => {
            this.message.success('Login successful!');
            sessionStorage.setItem('token', response.token);
            this.router.navigate(['/dashboard']);
          },
          error: (error) => {
            console.error(error);
            this.message.error('Login failed. Please check your credentials.');
          }
        });
    } else {
      Object.values(this.loginForm.controls).forEach(control => {
        if (control.invalid) {
          control.markAsDirty();
          control.updateValueAndValidity({ onlySelf: true });
        }
      });
    }
  }
  /***/
}
