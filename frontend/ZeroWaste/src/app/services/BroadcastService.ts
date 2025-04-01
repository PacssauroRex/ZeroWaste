import { Injectable } from "@angular/core";
import { Broadcast } from "../pages/broadcasts/broadcast";
import { API_URL } from "../utils/constants";


@Injectable({
  providedIn: 'root'
})

export class BroadcastService {

    public async getAllBroadcasts(): Promise<Broadcast[]> {
        const response = await fetch(API_URL + '/broadcasts', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });
        
        const { broadcast_lists } = await response.json();
        return broadcast_lists ?? [];
    }

}