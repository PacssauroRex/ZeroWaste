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

  public async triggerBroadcast(id: number): Promise<void> {
    const apiUrl = `${API_URL}/broadcasts/${id}/trigger`;

    const response = await fetch(apiUrl, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });

    if (response.status !== 200) {
      throw new Error('Erro ao enviar broadcast');
    }

    return;
  }
}
