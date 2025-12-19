using Microsoft.EntityFrameworkCore;
using ThAmCo.Events.Domain;

namespace ThAmCo.Events.Data
{
    public class EventsDbContext : DbContext
    {
        public DbSet<Staff> Staffs { get; set; } = null!;

        public DbSet<Staffing> Staffings { get; set; } = null!;

        public DbSet<Event> Events { get; set; } = null!;

        public DbSet<GuestBooking> GuestBookings { get; set; } = null!;

        public DbSet<Guest> Guests { get; set; } = null!;

        private string DbPath { get; set; } = string.Empty;

        // Constructor to set-up the database path & name
        public EventsDbContext()
        {
            var folder = Environment.SpecialFolder.MyDocuments;
            var path = Environment.GetFolderPath(folder);
            DbPath = Path.Join(path, "ThAmCo.Events.db");
        }

        // OnConfiguring to specify that the SQLite database engine will be used
        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            base.OnConfiguring(optionsBuilder);
            optionsBuilder.UseSqlite($"Data Source={DbPath}");
        }

        protected override void OnModelCreating(ModelBuilder builder)
        {
            base.OnModelCreating(builder);

            // Soft Delete for Events
            builder.Entity<Event>().HasQueryFilter(e => !e.IsDeleted);

            // Composite Key
            builder.Entity<Staffing>()
                .HasKey(sf => new { sf.EventId, sf.StaffId });

            // Composite Key
            builder.Entity<GuestBooking>()
                .HasKey(gb => new { gb.EventId, gb.GuestId });

            builder.Entity<Staff>()
                .HasMany(s => s.Staffings)
                .WithOne(sf => sf.Staff)
                .HasForeignKey(sf => sf.StaffId)
                .OnDelete(DeleteBehavior.Cascade);

            builder.Entity<Guest>()
                .HasMany(g => g.GuestBookings)
                .WithOne(gb => gb.Guest)
                .HasForeignKey(gb => gb.GuestId)
                .OnDelete(DeleteBehavior.Cascade);

            builder.Entity<Event>()
                .HasMany(e => e.Staffings)
                .WithOne(sf => sf.Event)
                .HasForeignKey(sf => sf.EventId)
                .OnDelete(DeleteBehavior.Cascade);

            builder.Entity<Event>()
                .HasMany(e => e.GuestBookings)
                .WithOne(gb => gb.Event)
                .HasForeignKey(gb => gb.EventId)
                .OnDelete(DeleteBehavior.Cascade);
        }
    }
}
