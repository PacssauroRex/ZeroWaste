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

    public async deleteBroadcast(id: number): Promise<void> {
        const response = await fetch(API_URL + '/broadcasts/' + id, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application',
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
            'Accept': 'application/json',
          },
        });
    
        if (!response.ok) {
          const body = await response.json();
    
          throw new Error('Error deleting broadcast', {
            cause: body,
          });
        }
      }
}