import { HttpClient, HttpHandler, HttpHeaders } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { AuthService } from './auth.service';
import { ArchitectInquiry } from '../models/architect-inquiry.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ArchitectInquiryService {
public apiUrl = 'https://8080-aeedbbeceebcafdebcbbbeecadaeebfaacbcba.project.examly.io';

constructor(private http:HttpClient,private authService :AuthService){}

  private getHeaders()
  {
    return {
    headers:new HttpHeaders({ Authorization :'Bearer'+this.authService.getToken()})
    };
  }

  addInquiry(inquiry:ArchitectInquiry):Observable<any>{
    return this.http.post(`${this.apiUrl}/api/inquiries`,inquiry, this.getHeaders());
  }

  getAllInquiries():Observable<ArchitectInquiry[]>{
    return this.http.get<ArchitectInquiry[]>(`${this.apiUrl}/api/inquiries`, this.getHeaders());
  }
  getInquiriesByUserId(userId:number):Observable<ArchitectInquiry[]>{
    return this.http.get<ArchitectInquiry[]>(`${this.apiUrl}/api/inquiries/user/${userId}`, this.getHeaders());
  }
  updateInquiry(inquiryId:number,inquiry:ArchitectInquiry):Observable<any>{
    return this.http.put(`${this.apiUrl}/api/inquiries/${inquiryId}`,inquiry, this.getHeaders());
  }

  deleteInquiry(inquiryId:number):Observable<any>{
    return this.http.delete(`${this.apiUrl}/api/inquiries/${inquiryId}`, this.getHeaders());
  }

}
