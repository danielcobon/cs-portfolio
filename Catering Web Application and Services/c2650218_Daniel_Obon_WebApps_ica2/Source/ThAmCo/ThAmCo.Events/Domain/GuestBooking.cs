using System.ComponentModel.DataAnnotations;

namespace ThAmCo.Events.Domain
{
    public class GuestBooking
    {
        [Required]
        public int EventId { get; set; }

        [Required]
        public int GuestId { get; set; }

        // Navigation property to Event (one-side of one-to-many)
        public Event Event { get; set; }

        // Navigation property to Guest (one-side of one-to-many)
        public Guest Guest { get; set; }
    }
}
