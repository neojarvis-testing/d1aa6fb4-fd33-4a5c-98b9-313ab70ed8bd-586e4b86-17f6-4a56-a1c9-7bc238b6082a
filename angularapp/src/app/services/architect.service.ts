import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { AuthService } from './auth.service';
import { Observable } from 'rxjs';
import { Architect } from '../models/architect.model';

@Injectable({
  providedIn: 'root'
})
export class ArchitectService {

  public apiUrl = 'https://8080-aeedbbeceebcafdebcbbbeecadaeebfaacbcba.project.examly.io';
  private http = inject(HttpClient);
private authService= inject(AuthService);

  private getHeaders()
  {
    const token = localStorage.getItem('token');

    return{
      headers:new HttpHeaders({ Authorization:`Bearer ${token}`})
    };
  }

  getAllArchitects():Observable<Architect[]>
  {
    return this.http.get<Architect[]>(`${this.apiUrl}/api/architects`,this.getHeaders());
  }

  
  getArchitectById(architectId:number):Observable<Architect>
  {
    return this.http.get<Architect>(`${this.apiUrl}/api/architects/${architectId}`,this.getHeaders());
  }

  addArchitect(architect:Architect):Observable<any>
  {
    return this.http.post(`${this.apiUrl}/api/architects`,architect,this.getHeaders());
  }
  
  updateArchitect(architectId:number, architect:Architect):Observable<any>
  {
    return this.http.put(`${this.apiUrl}/api/architects/${architectId}`,architect,this.getHeaders());
  }

  deleteArchitect(architectId:number):Observable<any>
  {
    return this.http.delete(`${this.apiUrl}/api/architects/${architectId}`,this.getHeaders());
  }





}
