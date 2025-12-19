using System.ComponentModel.DataAnnotations;

namespace ThAmCo.Events.Models
{

    public class CreateGuestViewModel
    {
        [Display(Name = "Guest Name")]
        public string GuestName { get; set; }

        [Display(Name = "Phone Number")]
        public string GuestPhone { get; set; }

        [Display(Name = "Email Address")]
        public string GuestEmail { get; set; }
    }
}
