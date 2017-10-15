package Model;

import java.sql.Timestamp;

import model.exceptions.VisitedLocationException;

public final class VisitedLocation {
	// object data
		private long id = 0;
		private long userId = 0;
		private long locationId = 0;
		private Timestamp datetime;
		
		// constructor to be used when inserting new visited location for user
		public VisitedLocation(long userId, long locationId, Timestamp datetime) throws VisitedLocationException {
			this.userId = userId;
			this.locationId = locationId;
			this.setDatetime(datetime);
		}

		// constructor to be used when loading an existing user from db
		public VisitedLocation(long id, long userId, long locationId, Timestamp datetime) throws VisitedLocationException{
			this(userId, locationId, datetime);
			this.id = id;
		}

		//accessors
		public long getId() {
			return this.id;
		}
		
		public long getUserId() {
			return this.userId;
		}
		
		public long getLocationId() {
			return this.locationId;
		}
		
		public Timestamp getDatetime() {
			return this.datetime;
		}

		
		//mutators
		public void setId(long id) {
			this.id = id;
		}
		
		private void setDatetime(Timestamp datetime) throws VisitedLocationException {
			if(datetime!=null) {
				this.datetime = datetime;
			} else {
				throw new VisitedLocationException("Invalid date/time!");
			}
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((datetime == null) ? 0 : datetime.hashCode());
			result = prime * result + (int) (id ^ (id >>> 32));
			result = prime * result + (int) (locationId ^ (locationId >>> 32));
			result = prime * result + (int) (userId ^ (userId >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			VisitedLocation other = (VisitedLocation) obj;
			if (datetime == null) {
				if (other.datetime != null)
					return false;
			} else if (!datetime.equals(other.datetime))
				return false;
			if (id != other.id)
				return false;
			if (locationId != other.locationId)
				return false;
			if (userId != other.userId)
				return false;
			return true;
		}

}