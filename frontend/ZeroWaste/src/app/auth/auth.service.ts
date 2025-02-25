import { Injectable } from '@angular/core';

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

