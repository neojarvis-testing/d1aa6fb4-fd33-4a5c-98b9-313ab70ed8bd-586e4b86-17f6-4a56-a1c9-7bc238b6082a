import { Injectable, inject } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { User } from '../models/user.model';
import { Login } from '../models/login.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  public apiUrl = 'https://8080-aeedbbeceebcafdebcbbbeecadaeebfaacbcba.project.examly.io';
  private userRole = new BehaviorSubject<String  | null>(null);
  private userId = new BehaviorSubject<number | null>(null);

  constructor(private http:HttpClient){}

  register(user:User) : Observable<any>{
    return this.http.post(`${this.apiUrl}/api/register`,user);
  }

  login(login:Login) : Observable<any>{
    return this.http.post(`${this.apiUrl}/api/login`,login);
  }

  setToken(token:string){
    localStorage.setItem('jwtToken',token);
  }
  getToken(): string|null{
    return localStorage.getItem('jwtToken');
  }

  setUserRole(role:string)
  {
    this.userRole.next(role);
  }

  setUserId(id:number)
  {
    this.userId.next(id);
  }
}
