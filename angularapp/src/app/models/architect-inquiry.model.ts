import { User } from "./user.model";
import { Architect } from "./architect.model";
export class ArchitectInquiry {
inquiryId?:number;
user:User;
architect:Architect;
message:string;
priority:string;
contactDetails?:string;
inquiryDate?:string;
status:string;
}
