/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;

import java.sql.ResultSetMetaData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cbrammer
 */
public class StoreDAO {

  /////////////////////////////////////////////
  ///   Singleton code

  private static StoreDAO instance = null;
  private Cache cache = Cache.getInstance();
  private ConnectionPool cp = ConnectionPool.getInstance();

  /** Creates a new instance of SkeletonDAO */
  private StoreDAO() {
  }//constructor

  /** Retrieves the single instance of this class */
  public static synchronized StoreDAO getInstance() {
    if (instance == null) {
      instance = new StoreDAO();
    }
    return instance;
  }//getInstance


  ////////////////////////////////////////////
  ///   CREATE methods

  /** Creates a new pkg in the database */
  public Store create(String id) throws DataException {
    // create new BO, set whether in the DB or not
      Store pkg = new Store(id);
      pkg.setObjectAlreadyInDB(false);

    // put into the cache
      cache.put(pkg.id, pkg);

    // return the new object
      return pkg;
      
  }//create



  ////////////////////////////////////////////
  ///   READ methods

  /** Reads an existing pkg from the database */
  public Store read(String id) throws DataException {
    // check cache
    Store pkg = (Store) cache.get(id);
    if(pkg != null) {
        return pkg;
    }

    // get a jdbc connection
    Connection conn = cp.get();
    try {
        // call the other read method
        pkg = read(id, conn);
    } catch (Exception ex) {
        throw new DataException("can't read Store", ex);
    } finally {
        cp.release(conn);
    }

    // use a finally clause to release the connection
    // return the object
    return pkg;
  }

  /** Internal method to read an existing pkg from the database */
  synchronized Store read(String id, Connection conn) throws Exception {
    // check cache
      Store pkg = (Store) cache.get(id);
      if(pkg != null) {
          return pkg;
      }
      pkg = new Store(id);
      cache.put(pkg.getId(), pkg);
    // pull from database and populate the object
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM store WHERE guid LIKE ?");
      pstmt.setString(1, id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
            pkg.setLocation(rs.getString(2));
            pkg.setManagersGuid(rs.getString(3));
            pkg.setAddress(rs.getString(4));
            pkg.setPhone(rs.getString(5));

      } else {
          throw new DataException("bad Store read");
      }
      if(pkg.getManagersGuid() != null && !pkg.getManagersGuid().equals("")) {
        Employee e = EmployeeDAO.getInstance().read(pkg.getManagersGuid(), conn);
        pkg.setManager(e);
      }
      pkg.setDirty(false);
      pkg.setObjectAlreadyInDB(true);
    // put in the cache


    // return the object

      return pkg;
  }//read


  /////////////////////////////////////////////
  ///   UPDATE methods

  /** Saves an existing pkg in the database */
  public void save(Store pkg) throws DataException {

      // get a jdbc connection
      Connection conn = cp.get();

      try {
          save(pkg, conn);
          // commit
          conn.commit();
      } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new DataException("can't roll back", e);
            }
            throw new DataException("Problem saving Store", e);
      } finally {
          ConnectionPool.getInstance().release(conn);
      }
  }//update

  /** Internal method to update a pkg in the database */
  void save(Store pkg, Connection conn) throws Exception {
    // update the cache
        cache.put(pkg.getId(), pkg);

        if(pkg.getManager() != null)
            EmployeeDAO.getInstance().save(pkg.getManager(), conn);
    // if not dirty, return
      if(!pkg.isDirty() && pkg.isObjectAlreadyInDB()) {
          return;
      }

    // call either update() or insert()
        if(pkg.isObjectAlreadyInDB()) {
            update(pkg, conn);
        } else {
            insert(pkg, conn);
        }
  }//save

   /** Saves an existing pkg to the database */
  private void update(Store pkg, Connection conn) throws Exception {
      PreparedStatement pstmt = conn.prepareStatement("UPDATE store SET location=?, managerGuid=?, address=?, phone=? WHERE guid LIKE ?");
    
      pstmt.setString(1, pkg.getLocation());
      pstmt.setString(2, pkg.getManagersGuid());
      pstmt.setString(3, pkg.getAddress());
      pstmt.setString(4, pkg.getPhone());
      pstmt.setString(5, pkg.getId());

      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad Store update");
      }

      pstmt.close();
  }

  /** Inserts a new pkg into the database */
  private void insert(Store pkg, Connection conn) throws Exception {
    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO store VALUES (?,?,?,?,?)");
      pstmt.setString(1, pkg.getId());
      pstmt.setString(2, pkg.getLocation());
      pstmt.setString(3, pkg.getManagersGuid());
      pstmt.setString(4, pkg.getAddress());
      pstmt.setString(5, pkg.getPhone());
      
      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad Store insert");
      }
      pstmt.close();
       
  }

 



  /////////////////////////////////////////////////
  ///   DELETE methods

  /** We do not support deleting of business objects in this application */
  public void delete(Store pkg) throws DataException {
    throw new UnsupportedOperationException("Nice try. The delete function is not supported in this application.");
  }



  ////////////////////////////////////////////////
  ///   SEARCH methods

  /** Retrieves all prods from the database */
  public java.util.List<Store> getAll() throws DataException {
      Connection conn = cp.get();
      LinkedList<Store> listAll = new LinkedList();
      PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement("SELECT guid id FROM store");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String myGuid = rs.getString(1);
                Store pkg;
                try {
                    pkg = read(myGuid, conn);
                } catch (Exception ex) {
                   throw new DataException("bad read Store");
                }
                listAll.add(pkg);
            }
        } catch (SQLException ex) {
            throw new DataException("bad read all Store");
        } finally {
          cp.release(conn);
        }
        return listAll;
  }


  // additional search methods go here.  examples are
  // getByName, getByProductCode, etc.


}
