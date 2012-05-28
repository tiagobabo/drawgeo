class ChangeColumnsAround < ActiveRecord::Migration
   def self.up
   	rename_column :draws, :id_creator, :old_id_creator
   	add_column :draws, :id_creator, :integer
   	Draw.reset_column_information
   	Draw.all.each {|e| e.update_attribute(:id_creator, e.old_id_creator.to_i) }
   	remove_column :draws, :old_id_creator
 end
end
