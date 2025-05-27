import { Injectable, inject } from '@angular/core';
import { AuthService } from './auth.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Feedback } from '../models/feedback.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FeedbackService {
public apiUrl = 'https://8080-aeedbbeceebcafdebcbbbeecadaeebfaacbcba.project.examly.io';

private getHeaders()
{
  return {
  headers:new HttpHeaders({ Authorization :'Bearer'+this.authservice.getToken()})
  };
}
constructor(private http:HttpClient,private authservice :AuthService){}
sendFeedback(feedback :Feedback):Observable<any>{
  return this.http.post(`${this.apiUrl}/api/feedback`,feedback,this.getHeaders());
}

getAllFeedbacksByUserId(userId:number):Observable<Feedback[]>{
  return this.http.get<Feedback[]>(`${this.apiUrl}/api/feedback/user/${userId}`,this.getHeaders());
}

deleteFeedback(feedbackId :number):Observable<any>{
  return this.http.delete(`${this.apiUrl}/api/feedback/${feedbackId}`,this.getHeaders());
}

getFeedbacks():Observable<Feedback[]>{
  return this.http.get<Feedback[]>(`${this.apiUrl}/api/feedback`,this.getHeaders());
}
}
