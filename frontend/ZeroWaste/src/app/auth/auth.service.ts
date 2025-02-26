import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';
import { User } from './user';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiURL = "http://localhost:8080";

  async login(email: string, password: string): Promise<boolean> {
    try{
      const resposta = await fetch(this.apiURL, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({ email, password }),
      });

      if(!resposta.ok){
        throw new Error('Login Falhou');
      }

      const data = await resposta.json();
      localStorage.setItem('token', data.token);
      return true;
    } catch (error) {
      console.error('Erro ao logar', error);
      return false;
    }
  }


  isAuthenticated(): boolean {
    return !! this.getToken();
  }

  hasRole(requiredRole: string): boolean {
    const token = this.getToken();
    if (!token) {
      return false;
    }
    try {
      const decodedToken: any = jwtDecode(token); 
      return decodedToken.role === requiredRole;
    } catch (error) {
      console.error('Erro de token', error);
      return false;    
    }

  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }
  
  async register(user: User): Promise<User | null> {
    const url = this.apiURL + '/users';
    try {
      const response = await fetch(url, {
        method:'POST',
        headers: { 'Content-Type': 'application/json'},
        body: JSON.stringify(user)
      });
      return await response.json();
    }
    catch (err) {
      console.error("Erro ao registrar novo usuário: ", err);
      return null;
    }
  }
}
