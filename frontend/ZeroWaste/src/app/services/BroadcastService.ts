import { Injectable } from "@angular/core";
import { Broadcast, CreateBroadcastDTO } from "../pages/broadcasts/broadcast";
import { API_URL } from "../utils/constants";


@Injectable({
  providedIn: 'root'
})

export class BroadcastService {
  public async createBroadcast(data: CreateBroadcastDTO): Promise<void> {
    const response = await fetch(API_URL + "/broadcasts", {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
      body: JSON.stringify(data),
    });

    const responseData = await response.json();

    return responseData;
  }

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
