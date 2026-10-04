# Disaster model

IDisaster exposes Initialize, ApplyForces, Update, Propagate, GetAffectedArea and Stop.

The selected sector is the origin only. Propagation is a physical field over spatial sectors, not radius deletion.

Earthquake parameters: horizontal/vertical acceleration in m/s^2, frequency in Hz, duration in s, and direction. The disaster applies inertial loading to structural masses; the structural solver evaluates response and connection failure.
