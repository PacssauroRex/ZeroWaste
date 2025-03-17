import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';
import { User } from './user';
import { API_URL } from '../utils/constants';

export type JWTPayload = {
  exp: number;
  iss: string;
  sub: string;
};

export type UserPayload = {
  role: "ADMIN" | "USER";
  email: string;
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  async login(email: string, password: string): Promise<boolean> {
    const url = API_URL + '/users/login'
    try {
      const resposta = await fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password }),
      });

      if (!resposta.ok) {
        throw new Error('Login Falhou');
      }

      const data = await resposta.json();
      const userPayload = JSON.parse(jwtDecode(data.token).sub!);

      localStorage.setItem('token', data.token);
      localStorage.setItem('user', JSON.stringify(userPayload));
      return true;
    } catch (error) {
      console.error('Erro ao logar', error);
      return false;
    }
  }


  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  hasRole(requiredRole: string): boolean {
    const token = this.getToken();
    if (!token) {
      return false;
    }
    try {
      const userData: UserPayload = JSON.parse(localStorage.getItem('user')!);

      return userData.role === requiredRole;
    } catch (error) {
      console.error('Erro de token', error);
      return false;
    }

  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  async register(user: User): Promise<User | null> {
    const url = API_URL + '/users';
    try {
      const response = await fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(user)
      });
      return await response.json();
    }
    catch (err) {
      console.error("Erro ao registrar novo usuário: ", err);
      return null;
    }
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  }
}
