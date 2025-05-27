import { Architect } from "./architect.model";
import { User } from "./user.model";

export class Feedback {
    feedbackId?:number;
    feedbackText:string;
    date:string;
    user:User;
    architect?:Architect;
    category:string;


}
