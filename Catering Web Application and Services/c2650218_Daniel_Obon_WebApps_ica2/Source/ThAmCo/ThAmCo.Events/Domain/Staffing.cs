using System.ComponentModel.DataAnnotations;

namespace ThAmCo.Events.Domain
{
    public class Staffing
    {
        [Required]
        public int EventId { get; set; }

        [Required]
        public int StaffId { get; set; }

        // Navigation property to Staff (one-side of one-to-many)
        public  Staff Staff { get; set; }

        // Navigation property to Event (one-side of one-to-many)
        public Event Event { get; set; }

    }
}
