import { Injectable } from '@angular/core';
import { jwtDecode} from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiURL = "http://localhost:1717/auth";

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
    const decodedToken: any = jwtDecode(token);
    return decodedToken.role === requiredRole;
  } catch (error) {
    console.error('Erro de token', error);
    return false;    
  }

getToken(): string | null {
  return localStorage.getIem('token');
}

}
