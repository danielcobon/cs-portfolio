using System.ComponentModel.DataAnnotations;
using System.Runtime.CompilerServices;

namespace ThAmCo.Events.Models
{

    public class EditGuestViewModel
    {
        public int GuestId { get; set; }
        [Display(Name = "Guest Name")]
        public string GuestName { get; set; }

        [Display(Name = "Guest Phone Number")]
        public string GuestPhone { get; set; }

        [Display(Name = "Guest Email Address")]
        public string GuestEmail { get; set;}

        [Display(Name = "Attendance")]
        public AttendanceStatus Attendance { get; set; } // AttendanceStatus is an enum
    }
}
